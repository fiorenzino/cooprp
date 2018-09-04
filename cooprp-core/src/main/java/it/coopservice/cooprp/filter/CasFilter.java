package it.coopservice.cooprp.filter;

import it.coopservice.cooprp.management.AppProperties;
import org.giavacms.commons.auth.cas.filter.AbstractCasFilter;

import javax.servlet.annotation.WebFilter;

@WebFilter(urlPatterns = {"*.jsf", "*.jsp", "/private/index.html", "/private2/index.html" })
public class CasFilter extends AbstractCasFilter
{
    @Override
    protected String getCasServerLoginUrl() {
        return AppProperties.casServerLoginUrl.value();
    }

    @Override
    protected String getCasExcluded() {
        return AppProperties.casExcluded.value();
    }
}
