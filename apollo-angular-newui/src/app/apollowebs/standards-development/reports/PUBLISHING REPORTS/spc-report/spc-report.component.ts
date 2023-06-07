import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-spc-report',
  templateUrl: './spc-report.component.html',
  styleUrls: ['./spc-report.component.css']
})
export class SpcReportComponent implements OnInit {

  displayedColumns: string[] = ['projectId','ksNo','projectTitle', 'tcSecretary', 'division','date','status','comment'];

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
      
      status: ['', null],
      date: ['', null],
    });


  }

}