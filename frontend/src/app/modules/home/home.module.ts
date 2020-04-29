import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { NgxPermissionsModule } from 'ngx-permissions';
import { SharedModule } from 'src/app/shared/shared.module';

import { AdminHomeComponent } from './components/admin-home/admin-home.component';
import { HomeComponent } from './components/home/home.component';
import { InviteDialogComponent } from './components/invite-dialog/invite-dialog.component';
import { UserHomeComponent } from './components/user-home/user-home.component';

@NgModule({
  declarations: [HomeComponent, UserHomeComponent, AdminHomeComponent, InviteDialogComponent],
  imports: [CommonModule, SharedModule, NgxPermissionsModule.forChild()],
  exports: [HomeComponent],
  entryComponents: [InviteDialogComponent],
})
export class HomeModule {}
