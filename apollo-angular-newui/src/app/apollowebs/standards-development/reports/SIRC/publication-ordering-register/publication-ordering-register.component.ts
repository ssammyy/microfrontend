import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-publication-ordering-register',
  templateUrl: './publication-ordering-register.component.html',
  styleUrls: ['./publication-ordering-register.component.css']
})
export class PublicationOrderingRegisterComponent implements OnInit {

  displayedColumns: string[] = ['projectNo','dateReceived', 'requestReceived', 'customerName','requestAction','officerName','supplier','dateOrdered','invoiceNo','status','remark'];

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
      
      status: ['', null],
      requestAction: ['', null],
    });


  }

} 
