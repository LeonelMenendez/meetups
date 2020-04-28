import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent, InvitationsComponent } from './components';

const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'invitations', component: InvitationsComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class MenuRoutingModule {}
