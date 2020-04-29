import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { MatModule } from './mat.module';

@NgModule({
  declarations: [],
  imports: [MatModule, FormsModule, ReactiveFormsModule],
  exports: [MatModule, FormsModule, ReactiveFormsModule],
})
export class SharedModule {}
