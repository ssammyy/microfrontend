import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-ics-allocation',
  templateUrl: './ics-allocation.component.html',
  styleUrls: ['./ics-allocation.component.css']
})
export class IcsAllocationComponent implements OnInit {

  displayedColumns: string[] = ['projectNo','dateIssued', 'standardNo','icsNoIssued','IssuedBy','remarks'];

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
      
     
      icsNoIssued: ['', null],
    });


  }

} 
