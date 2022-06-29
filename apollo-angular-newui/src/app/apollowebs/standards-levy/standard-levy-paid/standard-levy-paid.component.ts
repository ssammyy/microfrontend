import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Subject, timer} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {
    PaidLevy,
    PaymentDetails
} from "../../../core/store/data/levy/levy.model";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {HttpErrorResponse} from "@angular/common/http";
import {DataTableDirective} from "angular-datatables";

@Component({
  selector: 'app-standard-levy-paid',
  templateUrl: './standard-levy-paid.component.html',
  styleUrls: ['./standard-levy-paid.component.css']
})
export class StandardLevyPaidComponent implements OnInit {
  tasks: PaidLevy[] = [];
  paymentDetails: PaymentDetails[] = [];
  paidDetails: PaymentDetails[] = [];
  pdfDetails: PaymentDetails[] = [];
  public actionRequest: PaymentDetails | undefined;
    public pdfRequest: PaymentDetails | undefined;

  dtElement: DataTableDirective;
  dtElements: DataTableDirective;

  dtTrigger: Subject<any> = new Subject<any>();
  dtTriggers: Subject<any> = new Subject<any>();
  isDtInitialized: boolean = false
  loadingText: string;
    isShow=true;
    blob: Blob;
    uploadedFiles: FileList;

  constructor(
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private levyService: LevyService,
  ) { }

  ngOnInit(): void {
    this.getLevyPayments();
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
    this.dtTriggers.unsubscribe();
  }
  // public getPaidLevies(): void{
  //   this.SpinnerService.show();
  //   this.levyService.getPaidLevies().subscribe(
  //       (response: PaidLevy[])=> {
  //         this.tasks = response;
  //         this.dtTrigger.next();
  //         this.SpinnerService.hide();
  //       },
  //       (error: HttpErrorResponse)=>{
  //         this.SpinnerService.hide();
  //         alert(error.message);
  //       }
  //   );
  // }




  public getLevyPayments(): void{
    this.loadingText = "Retrieving Payments ...."
    this.SpinnerService.show();
    this.levyService.getLevyPayments().subscribe(
        (response: PaymentDetails[]) => {
          this.paymentDetails = response;
          //console.log(this.paymentDetails);
          this.SpinnerService.hide();
          if (this.isDtInitialized) {
            this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
              dtInstance.destroy();
              this.dtTrigger.next();
              this.dtTriggers.next();
            });
          } else {
            this.isDtInitialized = true
            this.dtTrigger.next();
            this.dtTriggers.next();
          }
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          console.log(error.message);

        }
    );
  }
    // saveDetails() {
    //     let resultStatus = false;
    //     if (this.excelData.SchemesDetails) {
    //         this.spinnerService.show();
    //         Promise.all(this.excelData.SchemesDetails.map(async (item: any) => {
    //             const dataSave = new SchemeMapDto();
    //             dataSave.polName = item.polName;
    //             dataSave.smPolId = item.smPolId;
    //             dataSave.clnPolCode = item.clnPolCode;
    //             dataSave.countryCode = item.countryCode;
    //             dataSave.policyCurrencyCode = item.policyCurrencyCode;
    //             this.dataSaveList.push(dataSave);
    //         })).then(res => {
    //             console.log(this.dataSave);
    //             this.integrationDataService.saveVersionALLSchemeDetails(this.customerIntegID, this.dataSaveList).subscribe(
    //                 (response: ApiResponseModel) => {
    //                     this.spinnerService.hide();
    //                     this.savedData = response.data;
    //                     this.excelData.SchemesDetails = [];
    //                     resultStatus = true;
    //                     // this.integrationDataService.showSuccess(data.message);
    //                 },
    //                 (err: HttpErrorResponse) => {
    //                     this.spinnerService.hide();
    //                     console.log(err.error);
    //                     this.integrationDataService.showError(err.error);
    //                     resultStatus = false;
    //                 }
    //             );
    //         }).catch(error => {
    //             this.spinnerService.hide();
    //             this.showExportButtons = true;
    //             this.integrationDataService.showError(error.message);
    //         });
    //     }
    //     return resultStatus;
    // }
    getLevyPaymentsReceipt(id: number, fileName: string, applicationType: string): void {
        this.loadingText = "Generating E-Slip ...."
        this.SpinnerService.show();
        this.levyService.getLevyPaymentsReceipt(id).subscribe(
            (dataPdf: any) => {
                this.SpinnerService.hide();
                this.blob = new Blob([dataPdf], {type: applicationType});
                let downloadURL = window.URL.createObjectURL(this.blob);
                const link = document.createElement('a');
                link.href = downloadURL;
                link.download = fileName;
                link.click();
            },
            error => {
                this.SpinnerService.hide();
                console.log(error);
                this.levyService.showError('AN ERROR OCCURRED');
            },
        );
    }




    public onOpenModalPending(paidDetail: PaymentDetails, mode: string): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        //button.setAttribute('data-toggle', 'modal');
        if (mode === 'generatePdf') {
            this.pdfRequest = paidDetail;
            button.setAttribute('data-target', '#generatePdf');



        }

        container.appendChild(button);
        button.click();

    }

    public onOpenModalPayment(paymentDetail:PaymentDetails ,mode: string,companyId: number): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');

        if (mode === 'paymentDetails') {
            this.actionRequest = paymentDetail;
            button.setAttribute('data-target', '#paymentDetails');
            this.loadingText = "Loading ...."
            this.SpinnerService.show();
            this.levyService.getManufacturesLevyPaymentsList(companyId).subscribe(
                (response: PaymentDetails[]) => {
                    this.paidDetails = response;
                    this.SpinnerService.hide();
                    //console.log(this.paidDetails);
                    if (this.isDtInitialized) {
                        this.dtElements.dtInstance.then((dtInstance: DataTables.Api) => {
                            dtInstance.destroy();
                            this.dtTrigger.next();
                            this.dtTriggers.next();
                        });
                    } else {
                        this.isDtInitialized = true
                        this.dtTrigger.next();
                        this.dtTriggers.next();
                    }
                },
                (error: HttpErrorResponse) => {
                    this.SpinnerService.hide();
                    console.log(error.message);
                }
            );

        }

        // @ts-ignore
        container.appendChild(button);
        button.click();

    }


    viewPdfFile(): void {
        this.loadingText = "Loading...";
        this.SpinnerService.show();
        this.levyService.generatePdf().subscribe(
            (dataPdf: any) => {
                this.SpinnerService.hide();

                // tslint:disable-next-line:prefer-const
                let downloadURL = window.URL.createObjectURL(dataPdf);
                const link = document.createElement('a');
                link.href = downloadURL;
                link.click();
                // this.pdfUploadsView = dataPdf;
            },
        );
    }

}
