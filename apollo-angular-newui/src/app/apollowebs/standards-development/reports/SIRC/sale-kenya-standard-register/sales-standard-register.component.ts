import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-sales-standard-register',
  templateUrl: './sales-standard-register.component.html',
  styleUrls: ['./sales-standard-register.component.css']
})
export class SalesStandardRegisterComponent implements OnInit {

  displayedColumns: string[] = ['date','customerName','requestedPublication','quantity','totalAmount','receiptNo','chequeNo'];

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
      
      quantity: ['', null],
      totalAmount: ['', null],
      requestedPublication: ['', null],
    });


  }

} 
