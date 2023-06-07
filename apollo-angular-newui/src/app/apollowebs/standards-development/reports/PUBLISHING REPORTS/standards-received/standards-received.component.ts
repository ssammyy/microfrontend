import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';


@Component({
  selector: 'app-standards-received',
  templateUrl: './standards-received.component.html',
  styleUrls: ['./standards-received.component.css']
})
export class StandardsReceivedComponent implements OnInit {
  displayedColumns: string[] = ['report_id','slNo','title','stage', 'tc_secretary', 'division','date','comments'];

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
      
      division: ['', null],
      date: ['', null],
    });


  }

}