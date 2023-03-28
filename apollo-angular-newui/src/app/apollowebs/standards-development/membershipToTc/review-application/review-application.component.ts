import {Component, ElementRef, OnDestroy, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {Document, ReviewApplicationTask} from "../../../../core/store/data/std/request_std.model";
import {MembershipToTcService} from "../../../../core/store/data/std/membership-to-tc.service";
import {HttpErrorResponse} from "@angular/common/http";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {NgForm} from '@angular/forms';
import {takeUntil} from "rxjs/operators";
import {Router} from "@angular/router";

@Component({
    selector: 'app-review-application',
    templateUrl: './review-application.component.html',
    styleUrls: ['./review-application.component.css']
})
export class ReviewApplicationComponent implements OnInit, OnDestroy {
    p = 1;
    p2 = 1;
    public tcTasks: ReviewApplicationTask[] = [];
    public actionRequest: ReviewApplicationTask | undefined;
    loadingText: string;
    public uploadedFiles: FileList;
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    dtTrigger2: Subject<any> = new Subject<any>();

    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    isDtInitialized: boolean = false
    blob: Blob;
    display = 'none'; //default Variable
    loading = false;
    private _unsubscribeSignal$: Subject<void> = new Subject();
    displayUsers: boolean = false;

    dtOptionsB: DataTables.Settings = {};
    docs !: Document[];


    constructor(private membershipToTcService: MembershipToTcService, private notifyService: NotificationService,
                private router: Router,
                private SpinnerService: NgxSpinnerService) {
    }

    ngOnInit(): void {
        this.dtOptions = {
            destroy: true,
        };
        this.getApplicationsForReview();

    }

    id: any = 'Review Applications';

    tabChange(ids: any) {
        this.id = ids;
        if (this.id == "Review Applications") {
            this.reloadCurrentRoute()
        }
    }

    public getApplicationsForReview(): void {
        this.loading = true
        this.loadingText = "Retrieving Applications Please Wait ...."
        this.SpinnerService.show();
        this.membershipToTcService.getApplicationsForReview().pipe(takeUntil(this._unsubscribeSignal$.asObservable()))
            .subscribe(
                (response: ReviewApplicationTask[]) => {
                    this.tcTasks = response;
                    this.rerender()
                    this.displayUsers = true;

                    this.SpinnerService.hide();
                },
                (error: HttpErrorResponse) => {
                    this.SpinnerService.hide();
                    this.displayUsers = true;

                    alert(error.message);
                }
            )
    }

    public onOpenModal(task: ReviewApplicationTask): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        console.log(task.taskId);
        this.actionRequest = task;
        button.setAttribute('data-target', '#decisionModal');

        // @ts-ignore
        container.appendChild(button);
        button.click();
    }

    @ViewChild('closeViewModal') private closeModal: ElementRef;

    public hideModel() {
        this.closeModal?.nativeElement.click();
    }

    @ViewChild('editForm') private mytemplateForm: NgForm;
    pdfSrc: any;

    public clearForm() {
        this.mytemplateForm?.resetForm();
    }

    showToasterError(title: string, message: string) {
        this.notifyService.showError(message, title)

    }

    public decisionOnApplications(reviewApplicationTask: ReviewApplicationTask, tCApplicationId: number): void {

        if (reviewApplicationTask.comments_by_hof === "") {
            this.showToasterError("Error", `Please Enter A Comment.`);

        } else {
            this.SpinnerService.show();
            this.loadingText = "Approving Applicant";
            this.membershipToTcService.decisionOnApplications(reviewApplicationTask, tCApplicationId).subscribe(
                (response) => {
                    console.log(response);
                    this.getApplicationsForReview();
                    this.SpinnerService.hide();

                },
                (error: HttpErrorResponse) => {
                    alert(error.message);
                }
            )

            this.hideModel();
            this.clearForm();
        }

    }


    viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
        this.SpinnerService.show();
        this.membershipToTcService.viewDEditedApplicationPDF(pdfId, "ApplicantCV").subscribe(
            (dataPdf: any) => {
                this.SpinnerService.hide();
                this.blob = new Blob([dataPdf], {type: applicationType});

                // tslint:disable-next-line:prefer-const
                let downloadURL = window.URL.createObjectURL(this.blob);
                const link = document.createElement('a');
                link.href = downloadURL;
                this.pdfSrc = downloadURL
                console.log(downloadURL)
                link.download = fileName;
                link.click();
                // this.pdfUploadsView = dataPdf;
            },
        );
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
            this.dtTrigger2.next();


        });

    }

    ngOnDestroy(): void {
        // Do not forget to unsubscribe the event
        this.dtTrigger.unsubscribe();
        this.dtTrigger2.unsubscribe();

        this._unsubscribeSignal$.next();
        this._unsubscribeSignal$.unsubscribe();

    }

    reloadCurrentRoute() {
        let currentUrl = this.router.url;
        this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
            this.router.navigate([currentUrl]);
        });
    }


}
