package config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Enumeration;
@Component

public class InputSanitizerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);

            if (paramValues != null) {
                for (int i = 0; i < paramValues.length; i++) {
                    paramValues[i] = Jsoup.clean(paramValues[i], Safelist.basic());
                }
                request.setAttribute(paramName, paramValues);
            }
        }

        return true;
    }
}
