import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {ISAdoptionProposal, PublicReviewDrafts} from "../../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";
import {Store} from "@ngrx/store";
import {FormBuilder} from "@angular/forms";
import {StdIntStandardService} from "../../../../core/store/data/std/std-int-standard.service";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";

@Component({
  selector: 'app-comment-on-public-review',
  templateUrl: './comment-on-public-review.component.html',
  styleUrls: ['./comment-on-public-review.component.css']
})
export class CommentOnPublicReviewComponent implements OnInit {
  submitted = false;
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  dtTrigger1: Subject<any> = new Subject<any>();
  drafts: PublicReviewDrafts[]=[];
  public actionRequest: PublicReviewDrafts | undefined;
  encryptedId: string;
  loadingText: string;

  constructor(
      private store$: Store<any>,
      private formBuilder: FormBuilder,
      private stdIntStandardService : StdIntStandardService,
      private stdComStandardService:StdComStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private activatedRoute: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe(
        rs => {
          this.encryptedId = rs.get('encryptedId');

        },
    );
    this.getPublicReviewDraft(this.encryptedId);
  }

  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
    this.dtTrigger1.unsubscribe();
  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)

  }
  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }

  rerender(): void {
    this.dtElements.forEach((dtElement: DataTableDirective) => {
      if (dtElement.dtInstance)
        dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
          dtInstance.destroy();
        });
    });
    setTimeout(() => {
      this.dtTrigger.next();
      this.dtTrigger1.next();
    });
  }

  public getPublicReviewDraft(encryptedId: string): void{
    this.loadingText = "Retrieving Public Review Drafts ...."
    this.SpinnerService.show();
    this.stdIntStandardService.getPublicReviewDraft(encryptedId).subscribe(
        (response: PublicReviewDrafts[]) => {
          this.drafts = response;
          this.SpinnerService.hide();
          this.rerender();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          console.log(error.message);

        }
    );
  }

  public onOpenModal(draft: PublicReviewDrafts,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='comment'){
      this.actionRequest=draft;
      button.setAttribute('data-target','#commentModal');


    }
    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

}
