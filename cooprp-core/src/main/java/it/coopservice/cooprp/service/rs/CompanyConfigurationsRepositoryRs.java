package it.coopservice.cooprp.service.rs;

import it.coopservice.cooprp.management.AppConstants;
import it.coopservice.cooprp.model.CompanyConfiguration;
import it.coopservice.cooprp.repository.CompanyConfigurationsRepository;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.commons.jwt.annotation.AccountTokenVerification;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path(AppConstants.COMPANY_CONFIGURATIONS_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@AccountTokenVerification
public class CompanyConfigurationsRepositoryRs extends RsRepositoryService<CompanyConfiguration> {
    public CompanyConfigurationsRepositoryRs() {
    }

    @Inject
    public CompanyConfigurationsRepositoryRs(CompanyConfigurationsRepository companyConfigurationsRepository) {
        super(companyConfigurationsRepository);
    }
    
}