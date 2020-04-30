import { NgModule } from '@angular/core';
import { FlexLayoutModule } from '@angular/flex-layout';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { MatModule } from './mat.module';

@NgModule({
  declarations: [],
  imports: [MatModule, FormsModule, ReactiveFormsModule, FlexLayoutModule],
  exports: [MatModule, FormsModule, ReactiveFormsModule, FlexLayoutModule],
})
export class SharedModule {}
