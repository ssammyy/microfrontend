import { Component, OnInit } from '@angular/core';
import {StandardRequestService} from "../../../shared/services/standard-request.service";
import {FormControl, FormGroup, NgForm} from "@angular/forms";
import {Department, Product, StandardRequest, TechnicalCommittee} from "./standardrequest";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-standard-request',
  templateUrl: './standard-request.component.html',
  styleUrls: ['./standard-request.component.css']
})
export class StandardRequestComponent implements OnInit {

  public products : Product[] | undefined;
  public departments: Department[] | undefined;
  private committees: TechnicalCommittee[] | undefined;

  constructor(private standardRequestService:StandardRequestService) { }



  ngOnInit() {
    this.getStandards();
    this.getDepartments();
  }

  public getStandards(): void{
    this.standardRequestService.getStandards().subscribe(
      (response: Product[])=> {
        this.products = response;
      },
      (error: HttpErrorResponse)=>{
        alert(error.message);
      }
    );
  }
  public getDepartments(): void{
    this.standardRequestService.getDepartments().subscribe(
      (response: Department[])=> {
        this.departments = response;
      },
      (error: HttpErrorResponse)=>{
        alert(error.message);
      }
    );
  }

  public getTechnicalCommittee(): void{
    this.standardRequestService.getTechnicalCommittee().subscribe(
      (response: TechnicalCommittee[])=> {
        this.committees = response;
      },
      (error: HttpErrorResponse)=>{
        alert(error.message);
      }
    );
  }

  public saveStandard(savestandard: NgForm): void{

    this.standardRequestService.addStandardRequest(savestandard.value).subscribe(
      (response:StandardRequest) =>{
        console.log(response);
        savestandard.resetForm();
      },
      (error:HttpErrorResponse) =>{
        alert(error.message);
        savestandard.resetForm();
      }
    )
  }


}
