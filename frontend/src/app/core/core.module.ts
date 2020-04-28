import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgxPermissionsModule } from 'ngx-permissions';

import { SharedModule } from '../shared/shared.module';
import { HeaderComponent } from './header/header.component';

@NgModule({
  declarations: [HeaderComponent],
  imports: [SharedModule, RouterModule, NgxPermissionsModule],
  exports: [HeaderComponent],
})
export class CoreModule {}
