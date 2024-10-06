package r16a.Athena.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import r16a.Athena.services.IpRequestTrackingService;

import java.io.IOException;

@Component
@AllArgsConstructor
public class IpRequestFilter implements Filter {
    private IpRequestTrackingService ipRequestTrackingService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String ip = httpRequest.getRemoteAddr();
        String route = httpRequest.getRequestURI();

        if (ipRequestTrackingService.isBlocked(ip, route)) {
            httpResponse.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "Too many requests from your IP.");
            return;
        }

        ipRequestTrackingService.registerRequest(ip, route);
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
