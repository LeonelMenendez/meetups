import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from '../home/components/home/home.component';
import { InvitationsComponent } from '../invitations/components/invitations/invitations.component';

const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'invitations', component: InvitationsComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class MenuRoutingModule {}
