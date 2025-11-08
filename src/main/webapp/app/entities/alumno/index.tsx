import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Alumno from './alumno';
import AlumnoDetail from './alumno-detail';
import AlumnoUpdate from './alumno-update';
import AlumnoDeleteDialog from './alumno-delete-dialog';

const AlumnoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Alumno />} />
    <Route path="new" element={<AlumnoUpdate />} />
    <Route path=":id">
      <Route index element={<AlumnoDetail />} />
      <Route path="edit" element={<AlumnoUpdate />} />
      <Route path="delete" element={<AlumnoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AlumnoRoutes;
