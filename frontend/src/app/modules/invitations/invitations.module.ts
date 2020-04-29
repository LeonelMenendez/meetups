import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';

import { InvitationsComponent } from './components/invitations/invitations.component';

@NgModule({
  declarations: [InvitationsComponent],
  imports: [CommonModule, SharedModule],
  exports: [InvitationsComponent],
})
export class InvitationsModule {}
