import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbCollapseModule } from '@ng-bootstrap/ng-bootstrap';

import { MatModule } from './mat.module';

@NgModule({
  declarations: [],
  imports: [MatModule, FormsModule, ReactiveFormsModule, NgbCollapseModule],
  exports: [MatModule, FormsModule, ReactiveFormsModule, NgbCollapseModule],
})
export class SharedModule {}
