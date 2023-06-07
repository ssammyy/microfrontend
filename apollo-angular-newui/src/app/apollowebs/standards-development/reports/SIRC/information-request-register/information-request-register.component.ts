import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-information-request-register',
  templateUrl: './information-request-register.component.html',
  styleUrls: ['./information-request-register.component.css']
})
export class InformationRequestRegisterComponent implements OnInit {

  displayedColumns: string[] = ['projectNo','dateReceived', 'requestReceived', 'customerName','requestNature','availability','action','officerName','dateCompletion','timeline','remark'];

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
      
      timeline: ['', null],
      requestNature: ['', null],
    });


  }

} 