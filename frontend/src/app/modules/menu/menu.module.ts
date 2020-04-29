import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';

import { HomeModule } from '../home/home.module';
import { InvitationsModule } from '../invitations/invitations.module';
import { MenuComponent } from './components/menu.component';
import { MenuRoutingModule } from './menu-routing.module';

@NgModule({
  declarations: [MenuComponent],
  imports: [SharedModule, MenuRoutingModule, HomeModule, InvitationsModule],
  providers: [],
})
export class MenuModule {}
