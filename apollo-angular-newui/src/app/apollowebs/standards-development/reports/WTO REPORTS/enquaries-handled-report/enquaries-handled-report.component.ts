import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-enquaries-handled-report',
  templateUrl: './enquaries-handled-report.component.html',
  styleUrls: ['./enquaries-handled-report.component.css']
})
export class EnquariesHandledReportComponent implements OnInit {
 
  displayedColumns: string[] = ['snNumber', 'date', 'organization', 'natureOfInquiry', 'dateOfResponse', 'dateOfClosing'];

  searchFormGroup!: FormGroup;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  // dataSource!: MatTableDataSource<>;

  constructor(
    private formBuilder: FormBuilder,
   ) {
   }

  ngOnInit(): void {

    this.searchFormGroup = this.formBuilder.group({
      
      dateOfClosing: ['', null],
      dateOfResponse: ['', null],
     
    });


  }

} 


