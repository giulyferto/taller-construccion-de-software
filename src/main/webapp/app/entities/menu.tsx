import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/alumno">
        <Translate contentKey="global.menu.entities.alumno" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/curso">
        <Translate contentKey="global.menu.entities.curso" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/profesor">
        <Translate contentKey="global.menu.entities.profesor" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
