import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Person, samplePeopleData} from "./person";
import {Observable, of, Subject} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {DataService} from "./services/data.service";
// import sampleData from './data/data.json';


@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css']
})
export class TableComponent implements OnInit, OnDestroy {

  dtOptions: DataTables.Settings = {};
  persons: Person[] = [];
  public allPeopleData = samplePeopleData;


  // We use this trigger because fetching the list of persons can be quite long,
  // thus we ensure the data is fetched before rendering
  dtTrigger: Subject<any> = new Subject<any>();

  constructor(private http: HttpClient,
              public fetchDataService: DataService) {
  }

  ngOnInit(): void {
    this.dtOptions = {
      pagingType: 'full_numbers',
      pageLength: 5,
      paging: true,
      processing: true
    };

    this.fetchDataService.getAllPeopleData().subscribe(data => {
      this.persons = data;
      this.dtTrigger.next();
    });

  }

  ngOnDestroy(): void {
    // Do not forget to unsubscribe the event
    this.dtTrigger.unsubscribe();
  }

}
