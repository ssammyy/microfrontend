import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-membership-subcription-scheme',
  templateUrl: './membership-subcription-scheme.component.html',
  styleUrls: ['./membership-subcription-scheme.component.css']
})
export class MembershipSubcriptionSchemeComponent implements OnInit {

  displayedColumns: string[] = ['projectNo','memberName','totalAmount','deposit','membershipFee','chequeNo','jul','aug','sept','oct','nov','dec','jan','feb','mar','apr','may','jun','renewalDate',];

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
      
      memberName: ['', null],
      renewalDate: ['', null],
      
    });


  }

} 
