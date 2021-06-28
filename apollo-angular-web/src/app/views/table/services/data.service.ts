import { Injectable } from '@angular/core';
import {Observable, of} from "rxjs";
import {Person, samplePeopleData} from "../person";

@Injectable({
  providedIn: 'root'
})
export class DataService {

  constructor() { }

  getAllPeopleData(): Observable<Person[]> {
    let allPeople: Observable<Person[]>;
    allPeople = of(samplePeopleData);
    return allPeople;
  }



}
