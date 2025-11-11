import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('Profesor e2e test', () => {
  const profesorPageUrl = '/profesor';
  const profesorPageUrlPattern = new RegExp('/profesor(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const profesorSample = { nombre: 'tinted pfft properly' };

  let profesor;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/profesors+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/profesors').as('postEntityRequest');
    cy.intercept('DELETE', '/api/profesors/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (profesor) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/profesors/${profesor.id}`,
      }).then(() => {
        profesor = undefined;
      });
    }
  });

  it('Profesors menu should load Profesors page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('profesor');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Profesor').should('exist');
    cy.url().should('match', profesorPageUrlPattern);
  });

  describe('Profesor page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(profesorPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Profesor page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/profesor/new$'));
        cy.getEntityCreateUpdateHeading('Profesor');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', profesorPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/profesors',
          body: profesorSample,
        }).then(({ body }) => {
          profesor = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/profesors+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/profesors?page=0&size=20>; rel="last",<http://localhost/api/profesors?page=0&size=20>; rel="first"',
              },
              body: [profesor],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(profesorPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Profesor page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('profesor');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', profesorPageUrlPattern);
      });

      it('edit button click should load edit Profesor page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Profesor');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', profesorPageUrlPattern);
      });

      it('edit button click should load edit Profesor page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Profesor');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', profesorPageUrlPattern);
      });

      it('last delete button click should delete instance of Profesor', () => {
        cy.intercept('GET', '/api/profesors/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('profesor').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', profesorPageUrlPattern);

        profesor = undefined;
      });
    });
  });

  describe('new Profesor page', () => {
    beforeEach(() => {
      cy.visit(`${profesorPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Profesor');
    });

    it('should create an instance of Profesor', () => {
      cy.get(`[data-cy="nombre"]`).type('swanling industrialize dazzling');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'swanling industrialize dazzling');

      cy.get(`[data-cy="apellido"]`).type('adult essential whereas');
      cy.get(`[data-cy="apellido"]`).should('have.value', 'adult essential whereas');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        profesor = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', profesorPageUrlPattern);
    });
  });
});
