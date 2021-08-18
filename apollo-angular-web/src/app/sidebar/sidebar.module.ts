import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { SidebarComponent } from './sidebar.component';
import {MatButtonModule} from '@angular/material/button';

@NgModule({
    imports: [RouterModule, CommonModule, MatButtonModule, MatButtonModule, CommonModule],
    declarations: [ SidebarComponent ],
    exports: [ SidebarComponent ]
})

export class SidebarModule {}
