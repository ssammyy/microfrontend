import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-sale-foreign-standard-register',
  templateUrl: './sale-foreign-standard-register.component.html',
  styleUrls: ['./sale-foreign-standard-register.component.css']
})
export class SaleForeignStandardRegisterComponent implements OnInit {

  displayedColumns: string[] = ['date','customerName','requestedPublication','supplier','quantity','totalAmount','receiptNo','chequeNo'];

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
