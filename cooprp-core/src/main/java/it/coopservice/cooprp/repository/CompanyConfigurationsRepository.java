package it.coopservice.cooprp.repository;

import it.coopservice.cooprp.model.CompanyConfiguration;
import org.giavacms.api.repository.Search;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.Map;

@Stateless
@LocalBean
public class CompanyConfigurationsRepository extends BaseRepository<CompanyConfiguration>
{
    @Override
    protected void applyRestrictions(Search<CompanyConfiguration> search, String alias, String separator,
             StringBuffer sb,
             Map<String, Object> params) throws Exception
    {
        super.applyRestrictions(search, alias, separator, sb, params);

    }

}
