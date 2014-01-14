package org.apache.tapestry5.csrfprotection.internal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry5.csrfprotection.CsrfToken;
import org.apache.tapestry5.csrfprotection.services.CsrfTokenRepository;

/**
 * A delegate to Spring's {@link org.springframework.security.web.csrf.CsrfTokenRepository}.
 */
public class SpringCsrfTokenRepository implements CsrfTokenRepository
{
    private final org.springframework.security.web.csrf.CsrfTokenRepository springCsrfTokenRepository;

    private final HttpServletRequest request;

    private final HttpServletResponse response;

    /**
     * @param springCsrfTokenRepository .
     * @param request .
     * @param response .
     */
    public SpringCsrfTokenRepository(
        org.springframework.security.web.csrf.CsrfTokenRepository springCsrfTokenRepository,
        HttpServletRequest request, HttpServletResponse response)
    {
        super();
        this.springCsrfTokenRepository = springCsrfTokenRepository;
        this.request = request;
        this.response = response;
    }

    @Override
    public CsrfToken generateToken()
    {
        return mapToken(springCsrfTokenRepository.generateToken(request));
    }

    @Override
    public void saveToken(CsrfToken token)
    {
        springCsrfTokenRepository.saveToken(
            new org.springframework.security.web.csrf.DefaultCsrfToken(token.getHeaderName(), token.getParameterName(),
                token.getToken()), request, response);
    }

    @Override
    public CsrfToken loadToken()
    {
        return mapToken(springCsrfTokenRepository.loadToken(request));
    }

    private static CsrfToken mapToken(org.springframework.security.web.csrf.CsrfToken springToken)
    {
        return springToken == null ? null : new DefaultCsrfToken(springToken.getParameterName(),
            springToken.getParameterName(),
            springToken.getToken());
    }
}
