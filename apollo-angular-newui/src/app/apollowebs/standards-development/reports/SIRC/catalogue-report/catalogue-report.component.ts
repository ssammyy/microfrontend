import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";

@Component({
  selector: 'app-catalogue-report',
  templateUrl: './catalogue-report.component.html',
  styleUrls: ['./catalogue-report.component.css']
})
export class CatalogueReportComponent implements OnInit {

  displayedColumns: string[] = ['standardNo','standardTitle','publicationDate','gazette','tcNo','edition','division','status'];

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
      
      standardTitle: ['', null],
      status: ['', null],
     
    });


  }

} 


