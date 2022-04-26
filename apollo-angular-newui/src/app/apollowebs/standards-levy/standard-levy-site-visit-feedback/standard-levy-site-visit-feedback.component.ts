import {Component, OnInit, ViewChild} from '@angular/core';
import {Subject} from "rxjs";
import {
    DocumentDTO,
    ManufactureCompletedTask,
    ManufacturePendingTask,
    SiteVisitRemarks
} from "../../../core/store/data/levy/levy.model";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";
import {DataTableDirective} from "angular-datatables";

@Component({
  selector: 'app-standard-levy-site-visit-feedback',
  templateUrl: './standard-levy-site-visit-feedback.component.html',
  styleUrls: ['./standard-levy-site-visit-feedback.component.css']
})
export class StandardLevySiteVisitFeedbackComponent implements OnInit {
    @ViewChild(DataTableDirective, {static: false})
    dtElement: DataTableDirective;

    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    isDtInitialized: boolean = false

  manufacturePendingTasks: ManufacturePendingTask[] = [];
  public actionRequestPending: ManufacturePendingTask | undefined;
  blob: Blob;
    isShowRemarksTab= true;
    isShowDocumentsTab= true;
    documentDTOs: DocumentDTO[] = [];
   siteVisitRemarks: SiteVisitRemarks[] = [];
    loadingText: string;
  constructor(
      private levyService: LevyService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.viewFeedBackReport();
  }
    public viewFeedBackReport(): void{
        this.loadingText = "Loading ...."
        this.SpinnerService.show();
        this.levyService.viewFeedBackReport().subscribe(
            (response: ManufacturePendingTask[])=> {
                console.log(this.manufacturePendingTasks)
                this.manufacturePendingTasks = response;
                this.SpinnerService.hide();
                if (this.isDtInitialized) {
                    this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
                        dtInstance.destroy();
                        this.dtTrigger.next();
                    });
                } else {
                    this.isDtInitialized = true
                    this.dtTrigger.next();
                }

            },
            (error: HttpErrorResponse)=>{
                this.SpinnerService.hide();
                console.log(error.message);
            }
        );
    }
    toggleDisplayDocuments(visitId: number) {
        this.loadingText = "Loading ...."
        this.SpinnerService.show();
        this.levyService.getVisitDocumentList(visitId).subscribe(
            (response: DocumentDTO[]) => {
                this.documentDTOs = response;
                this.SpinnerService.hide();
                console.log(this.documentDTOs)
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                console.log(error.message);
            }
        );
        this.isShowDocumentsTab = !this.isShowDocumentsTab;
        this.isShowRemarksTab = true;

    }


  public onOpenModalPending(manufacturePendingTask: ManufacturePendingTask,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='viewPending'){
      this.actionRequestPending=manufacturePendingTask;
      button.setAttribute('data-target','#viewPending');
    }
    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
  viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
      this.loadingText = "Loading ...."
    this.SpinnerService.show();
    this.levyService.viewReportDoc(pdfId).subscribe(
        (dataPdf: any) => {
          this.SpinnerService.hide();
          this.blob = new Blob([dataPdf], {type: applicationType});

          // tslint:disable-next-line:prefer-const
          let downloadURL = window.URL.createObjectURL(this.blob);
          const link = document.createElement('a');
          link.href = downloadURL;
          link.download = fileName;
          link.click();
          // this.pdfUploadsView = dataPdf;
        },
    );
  }
    toggleDisplayRemarksTab(siteVisitId: number){
        this.loadingText = "Loading ...."
        this.SpinnerService.show();
        this.levyService.getSiteVisitRemarks(siteVisitId).subscribe(
            (response: SiteVisitRemarks[]) => {
                this.siteVisitRemarks = response;
                this.SpinnerService.hide();
                console.log(this.siteVisitRemarks)
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                console.log(error.message);
            }
        );
        this.isShowRemarksTab = !this.isShowRemarksTab;
        this.isShowDocumentsTab= true;
    }

}
