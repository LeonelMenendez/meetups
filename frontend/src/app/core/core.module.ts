import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { NgbCollapseModule } from '@ng-bootstrap/ng-bootstrap';

import { MatModule } from '../shared/modules/mat/mat.module';
import { HeaderComponent } from './header/header.component';

@NgModule({
  declarations: [HeaderComponent],
  imports: [CommonModule, MatModule, NgbCollapseModule],
  exports: [HeaderComponent],
})
export class CoreModule {}
