import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-standards-typeset',
  templateUrl: './standards-typeset.component.html',
  styleUrls: ['./standards-typeset.component.css']
})
export class StandardsTypesetComponent implements OnInit {

  displayedColumns: string[] = ['report_id','slNo','title','tc_secretary','date_received', 'division','stage','type_setter','date_completed','comments'];

  searchFormGroup!: FormGroup;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
//  dataSource!: MatTableDataSource<>;

  constructor(
    private formBuilder: FormBuilder,
    ) {
   }

  ngOnInit(): void {

    this.searchFormGroup = this.formBuilder.group({
      
      division: ['', null],
      date_received: ['', null],
      date_completed: ['', null],
    });


  }

}
