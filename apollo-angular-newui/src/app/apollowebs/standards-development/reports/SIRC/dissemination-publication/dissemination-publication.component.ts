import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-dissemination-publication',
  templateUrl: './dissemination-publication.component.html',
  styleUrls: ['./dissemination-publication.component.css']
})
export class DisseminationPublicationComponent implements OnInit {

  displayedColumns: string[] = ['projectNo','dateReceived', 'requestReceived','requestNature','customerName','officerName','quantity','totalAmount'];

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
      
     
      requestNature: ['', null],
    });


  }

} 