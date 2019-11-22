package com.dy.Filter;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(dispatcherTypes = {
		DispatcherType.REQUEST, 
		DispatcherType.FORWARD, 
		DispatcherType.INCLUDE, 
		DispatcherType.ERROR
}, urlPatterns = { "/*" })

public class IPFilter implements Filter {
    private final Set<String> whites;
    private final Set<String> blacks;

    public IPFilter(String whiteList, String blackList) {
        whites = createSet(whiteList.split(","));
        blacks = createSet(blackList.split(","));
        if (!blacks.isEmpty() && !whites.isEmpty())
            throw new IllegalArgumentException("blacklist and whitelist at the same time?");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String ip = request.getRemoteAddr();
        if (accept(ip)) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    private boolean accept(String ip) {
        if (whites.isEmpty() && blacks.isEmpty())
            return true;

        if (!whites.isEmpty()) {
            for (String w : whites) {
                if (simpleMatch(ip, w))
                    return true;
            }
            return false;
        }

        if (blacks.isEmpty())
            throw new IllegalStateException("cannot happen");

        for (String b : blacks) {
            if (simpleMatch(ip, b))
                return false;
        }

        return true;
    }

    private Set<String> createSet(String[] split) {
        Set<String> set = new HashSet<>(split.length);
        for (String str : split) {
            str = str.trim();
            if (!str.isEmpty())
                set.add(str);
        }
        return set;
    }

    private boolean simpleMatch(String ip, String pattern) {
        String[] ipParts = pattern.split("\\*");
        for (String ipPart : ipParts) {
            int idx = ip.indexOf(ipPart);
            if (idx == -1)
                return false;

            ip = ip.substring(idx + ipPart.length());
        }

        return true;
    }

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
