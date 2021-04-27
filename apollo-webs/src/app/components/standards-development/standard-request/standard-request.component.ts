import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, NgForm} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";
import {StandardDevelopmentService} from "../../../shared/services/standard-development.service";
import {Product, StandardRequest, TechnicalCommittee,Department} from "../../../shared/models/standard-development";

@Component({
  selector: 'app-standard-request',
  templateUrl: './standard-request.component.html',
  styleUrls: ['./standard-request.component.css']
})
export class StandardRequestComponent implements OnInit {

  public products : Product[] | undefined;
  public departments: Department[] | undefined;
  public committees: TechnicalCommittee[] | undefined;

  constructor(private standardDevelopmentService:StandardDevelopmentService) { }



  ngOnInit() {
    this.getStandards();
    this.getDepartments();
    this.getTechnicalCommittee();
  }

  public getStandards(): void{
    this.standardDevelopmentService.getStandards().subscribe(
      (response: Product[])=> {
        this.products = response;
      },
      (error: HttpErrorResponse)=>{
        alert(error.message);
      }
    );
  }
  public getDepartments(): void{
    this.standardDevelopmentService.getDepartments().subscribe(
      (response: Department[])=> {
        this.departments = response;
      },
      (error: HttpErrorResponse)=>{
        alert(error.message);
      }
    );
  }

  public getTechnicalCommittee(): void{
    this.standardDevelopmentService.getTechnicalCommittee().subscribe(
      (response: TechnicalCommittee[])=> {
        this.committees = response;
      },
      (error: HttpErrorResponse)=>{
        alert(error.message);
      }
    );
  }

  public saveStandard(savestandard: NgForm): void{

    this.standardDevelopmentService.addStandardRequest(savestandard.value).subscribe(
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
