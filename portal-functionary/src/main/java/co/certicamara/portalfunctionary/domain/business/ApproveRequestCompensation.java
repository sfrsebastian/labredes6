/**
 * 
 */
package co.certicamara.portalfunctionary.domain.business;

import co.certicamara.portalfunctionary.infrastructure.client.rs.WorkflowManagerClient;
import co.certicamara.portalfunctionary.infrastructure.client.rs.entities.CaseInfo;
import co.certicamara.portalfunctionary.infrastructure.exceptions.IException;
import fj.data.Either;

/**
 * @author Lean Factory
 *
 */
public class ApproveRequestCompensation {

    ///////////////////////////////
    // Attributes
    ///////////////////////////////



    ///////////////////////////////
    // Constructor
    ///////////////////////////////



    ///////////////////////////////
    // Public Methods
    ///////////////////////////////


    public  static Either<IException, Boolean> documentUpdateByCaseIdFailCompensate (long caseInfo, WorkflowManagerClient workflowManagerClient)
    {

        Either<IException, Boolean> either = null;

        Either<IException, CaseInfo> eitherDeleteProcess = workflowManagerClient.deleteProcessInstance(caseInfo);


        if (eitherDeleteProcess.isLeft()){

            // Notificar
            either = Either.left(eitherDeleteProcess.left().value());

        }else {

            either = Either.right(Boolean.TRUE);

        }

        return either;

    }

}
