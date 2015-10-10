package co.certicamara.portalFunctionary.domain.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import co.certicamara.portalfunctionary.api.representations.TaskDTO;
import co.certicamara.portalfunctionary.domain.business.TaskBusiness;
import co.certicamara.portalfunctionary.infrastructure.client.rs.CustomerSettingsClient;
import co.certicamara.portalfunctionary.infrastructure.client.rs.DocumentManagerClient;
import co.certicamara.portalfunctionary.infrastructure.client.rs.WorkflowManagerClient;
import co.certicamara.portalfunctionary.infrastructure.config.PortalFunctionaryAddEventToRequestPublishQueuesInfo;
import co.certicamara.portalfunctionary.infrastructure.config.PortalFunctionaryRabbitMQConfig;
import co.certicamara.portalfunctionary.infrastructure.exceptions.IException;
import co.certicamara.portalfunctionary.infrastructure.exceptions.TechnicalException;
import fj.data.Either;
import gp.e3.autheo.client.dtos.TokenDTO;

public class TaskBusinessTest {

    private WorkflowManagerClient workflowManagerClient;
    private DocumentManagerClient documentManagerClient;
    private CustomerSettingsClient customerSettingsClient;
    private PortalFunctionaryAddEventToRequestPublishQueuesInfo addEventToRequestReportPublishQueuesInfo;
    private PortalFunctionaryRabbitMQConfig portalFunctionaryRabbitMQConfig;
    
    private TaskBusiness taskBusiness;
    private TokenDTO token;

    @Before
    public void setUp() {

        workflowManagerClient = Mockito.mock(WorkflowManagerClient.class);
        documentManagerClient = Mockito.mock(DocumentManagerClient.class);
        taskBusiness = new TaskBusiness(workflowManagerClient, documentManagerClient, customerSettingsClient, addEventToRequestReportPublishQueuesInfo, 
                portalFunctionaryRabbitMQConfig);
        token = new TokenDTO("value", "username", "organization", "role", "systemRole", "businessRole", "userDocumentType", "userDocumentNumber");
    }

    @After
    public void tearDown() {

        workflowManagerClient = null;
        taskBusiness = null;
    }

    @Test
    public void testGetUserTasks_OK() {

        String username = "mauricio";

        int listSize = 3;
        List<TaskDTO> expectedTasksList = new ArrayList<TaskDTO>();
        expectedTasksList.add(Mockito.mock(TaskDTO.class));
        expectedTasksList.add(Mockito.mock(TaskDTO.class));
        expectedTasksList.add(Mockito.mock(TaskDTO.class));

        Either<IException, List<TaskDTO>> expectedEither = Either.right(expectedTasksList);


        Mockito.when(workflowManagerClient.getUserTasks(username, token)).thenReturn(expectedEither);
        Either<IException, List<TaskDTO>> eitherResult = taskBusiness.getUserTasks(username, token);

        assertNotNull(eitherResult);
        assertNotNull(eitherResult.right());
        assertEquals(listSize, expectedTasksList.size());
        assertEquals(expectedTasksList.size(), eitherResult.right().value().size());
    }

    @Test
    public void testGetUserTasks_Empty_OK() {

        String username = "mauricio";

        int listSize = 0;
        List<TaskDTO> expectedTasksList = new ArrayList<TaskDTO>();

        Either<IException, List<TaskDTO>> expectedEither = Either.right(expectedTasksList);


        Mockito.when(workflowManagerClient.getUserTasks(username, token)).thenReturn(expectedEither);
        Either<IException, List<TaskDTO>> eitherResult = taskBusiness.getUserTasks(username, token);

        assertNotNull(eitherResult);
        assertNotNull(eitherResult.right());
        assertEquals(listSize, expectedTasksList.size());
        assertEquals(expectedTasksList.size(), eitherResult.right().value().size());

    }

    @Test
    public void testGetUserTasks_NOK() {

        String username = "mauricio";

        Either<IException, List<TaskDTO>> expectedEither = Either.left(new TechnicalException("error mesage"));

        Mockito.when(workflowManagerClient.getUserTasks(username, token)).thenReturn(expectedEither);
        Either<IException, List<TaskDTO>> eitherResult = taskBusiness.getUserTasks(username, token);

        assertNotNull(eitherResult);
        assertNotNull(eitherResult.left());
        assertEquals(true, eitherResult.left().value() instanceof TechnicalException);

    }

}