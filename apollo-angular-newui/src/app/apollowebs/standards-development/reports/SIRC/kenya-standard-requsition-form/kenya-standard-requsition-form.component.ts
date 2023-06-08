import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-kenya-standard-requsition-form',
  templateUrl: './kenya-standard-requsition-form.component.html',
  styleUrls: ['./kenya-standard-requsition-form.component.css']
})
export class KenyaStandardRequsitionFormComponent implements OnInit {

  displayedColumns: string[] = ['date','ksNo','quanityOrdered','requisitioningOfficer','receivingOfficer','dateReceived','quantityReceived','receivedBy'];

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
      
      quantityOrdered: ['', null],
      quantityReceived: ['', null],
     
    });


  }

} 

