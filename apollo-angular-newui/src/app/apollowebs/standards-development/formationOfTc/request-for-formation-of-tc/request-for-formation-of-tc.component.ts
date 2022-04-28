import { Component, OnInit } from '@angular/core';
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {ProposalForTC} from "../../../../core/store/data/std/request_std.model";
import {HttpErrorResponse} from "@angular/common/http";
import {FormationOfTcService} from "../../../../core/store/data/std/formation-of-tc.service";

@Component({
  selector: 'app-request-for-formation-of-tc',
  templateUrl: './request-for-formation-of-tc.component.html',
  styleUrls: ['./request-for-formation-of-tc.component.css']
})
export class RequestForFormationOfTCComponent implements OnInit {

  public itemId :string="1";
  public groupId :string="draft";
  public type: string="TCRelevantDocument";


  constructor(private  standardDevelopmentService: FormationOfTcService,
              private notifyService : NotificationService) { }

  ngOnInit(): void {
  }

  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)
  }

  public uploadProposalForTC(proposalForTC: ProposalForTC): void {
    console.log(proposalForTC);
    this.standardDevelopmentService.uploadProposalForTC(proposalForTC).subscribe(
        (response: ProposalForTC) => {
          console.log(response);
          this.showToasterSuccess("Success","Successfully submitted proposal for formation of TC")
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    )
  }

}
