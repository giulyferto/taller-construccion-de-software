import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Profesor from './profesor';
import ProfesorDetail from './profesor-detail';
import ProfesorUpdate from './profesor-update';
import ProfesorDeleteDialog from './profesor-delete-dialog';

const ProfesorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Profesor />} />
    <Route path="new" element={<ProfesorUpdate />} />
    <Route path=":id">
      <Route index element={<ProfesorDetail />} />
      <Route path="edit" element={<ProfesorUpdate />} />
      <Route path="delete" element={<ProfesorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProfesorRoutes;
