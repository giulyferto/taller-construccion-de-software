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

describe('Alumno e2e test', () => {
  const alumnoPageUrl = '/alumno';
  const alumnoPageUrlPattern = new RegExp('/alumno(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const alumnoSample = { dni: 25479, nombre: 'grumpy gah', apellido: 'overtrain biodegrade by', fechaNacimiento: '2025-11-08' };

  let alumno;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/alumnos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/alumnos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/alumnos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (alumno) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/alumnos/${alumno.id}`,
      }).then(() => {
        alumno = undefined;
      });
    }
  });

  it('Alumnos menu should load Alumnos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('alumno');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Alumno').should('exist');
    cy.url().should('match', alumnoPageUrlPattern);
  });

  describe('Alumno page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(alumnoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Alumno page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/alumno/new$'));
        cy.getEntityCreateUpdateHeading('Alumno');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', alumnoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/alumnos',
          body: alumnoSample,
        }).then(({ body }) => {
          alumno = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/alumnos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/alumnos?page=0&size=20>; rel="last",<http://localhost/api/alumnos?page=0&size=20>; rel="first"',
              },
              body: [alumno],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(alumnoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Alumno page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('alumno');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', alumnoPageUrlPattern);
      });

      it('edit button click should load edit Alumno page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Alumno');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', alumnoPageUrlPattern);
      });

      it('edit button click should load edit Alumno page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Alumno');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', alumnoPageUrlPattern);
      });

      it('last delete button click should delete instance of Alumno', () => {
        cy.intercept('GET', '/api/alumnos/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('alumno').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', alumnoPageUrlPattern);

        alumno = undefined;
      });
    });
  });

  describe('new Alumno page', () => {
    beforeEach(() => {
      cy.visit(`${alumnoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Alumno');
    });

    it('should create an instance of Alumno', () => {
      cy.get(`[data-cy="dni"]`).type('4985');
      cy.get(`[data-cy="dni"]`).should('have.value', '4985');

      cy.get(`[data-cy="nombre"]`).type('pfft but');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'pfft but');

      cy.get(`[data-cy="apellido"]`).type('apud how');
      cy.get(`[data-cy="apellido"]`).should('have.value', 'apud how');

      cy.get(`[data-cy="fechaNacimiento"]`).type('2025-11-07');
      cy.get(`[data-cy="fechaNacimiento"]`).blur();
      cy.get(`[data-cy="fechaNacimiento"]`).should('have.value', '2025-11-07');

      cy.get(`[data-cy="tipoAlumno"]`).select('PROMOCIONAL');

      cy.get(`[data-cy="notaPromedio"]`).type('12921.84');
      cy.get(`[data-cy="notaPromedio"]`).should('have.value', '12921.84');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        alumno = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', alumnoPageUrlPattern);
    });
  });
});
