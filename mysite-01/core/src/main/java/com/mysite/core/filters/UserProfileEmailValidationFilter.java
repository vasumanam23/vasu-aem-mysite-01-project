package com.mysite.core.filters;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestPathInfo;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

@Component(
        service = Filter.class,
        property = {
                "sling.filter.scope=REQUEST",
                "service.ranking:Integer=100000"
        }
)
public class UserProfileEmailValidationFilter implements Filter {

    private static final Pattern EMAIL_RX = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,63}$",
            Pattern.CASE_INSENSITIVE
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // no-op
    }

    @Override
    public void destroy() {
        // no-op
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        if (!(req instanceof SlingHttpServletRequest) || !(res instanceof SlingHttpServletResponse)) {
            chain.doFilter(req, res);
            return;
        }

        SlingHttpServletRequest request = (SlingHttpServletRequest) req;
        SlingHttpServletResponse response = (SlingHttpServletResponse) res;

        RequestPathInfo rpi = request.getRequestPathInfo();
        String resourcePath = rpi.getResourcePath();
        String[] selectors = rpi.getSelectors();

        boolean isUserPropsSave =
                "POST".equalsIgnoreCase(request.getMethod()) &&
                        resourcePath != null &&
                        resourcePath.startsWith("/home/users/") &&
                        selectors != null &&
                        Arrays.asList(selectors).contains("rw") &&
                        Arrays.asList(selectors).contains("userprops") &&
                        "html".equalsIgnoreCase(rpi.getExtension());

        if (!isUserPropsSave) {
            chain.doFilter(req, res);
            return;
        }

        String email = coalesce(
                request.getParameter("./profile/email"),
                request.getParameter("profile/email"),
                request.getParameter("email")
        );

        if (StringUtils.isBlank(email) || !EMAIL_RX.matcher(email).matches()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("Invalid email address format.");
            String msg = "Email must be a valid address (e.g., name@domain.tld).";
            response.setHeader("X-Reason", msg);                // many Granite views read this
            response.setHeader("X-Request-Message", msg);       // fallback (some consoles use it)
            response.getWriter().write(msg);
            return;
        }



        // Optional: domain allowlist
        // if (!StringUtils.endsWithIgnoreCase(email, "@company.com")) {
        //     response.sendError(400, "Email domain must be company.com");
        //     return;
        // }

        chain.doFilter(req, res);
    }

    private static String coalesce(String... vals) {
        for (String v : vals) {
            if (StringUtils.isNotBlank(v)) return v;
        }
        return null;
    }
}