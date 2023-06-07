import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {Document, ReviewApplicationTask} from "../../../../../core/store/data/std/request_std.model";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {MembershipToTcService} from "../../../../../core/store/data/std/membership-to-tc.service";
import {NotificationService} from "../../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import {NgForm} from "@angular/forms";
import {UserRegister} from "../../../../../shared/models/user";
import {ActivatedRoute} from "@angular/router";
import {MasterService} from "../../../../../core/store/data/master/master.service";

@Component({
    selector: 'app-all-applications',
    templateUrl: './all-applications.component.html',
    styleUrls: ['./all-applications.component.css']
})
export class AllApplicationsComponent implements OnInit {
    p = 1;
    p2 = 1;
    public tcTasks: ReviewApplicationTask[] = [];
    public actionRequest: ReviewApplicationTask | undefined;
    loadingText: string;
    public uploadedFiles: FileList;
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    blob: Blob;
    display = 'none'; //default Variable
    loading = false;
    docs !: Document[];
    dtOptionsB: DataTables.Settings = {};

    dtTrigger2: Subject<any> = new Subject<any>();
    public userDetails!: UserRegister;


    constructor(private membershipToTcService: MembershipToTcService, private notifyService: NotificationService,
                private route: ActivatedRoute,
                private masterService: MasterService,
                private SpinnerService: NgxSpinnerService) {
    }

    ngOnInit(): void {
        this.getApplicationsForReview();

    }

    public getApplicationsForReview(): void {
        this.loading = true
        this.loadingText = "Retrieving Applications Please Wait ...."
        this.SpinnerService.show();
        this.membershipToTcService.getAllApplications().subscribe(
            (response: ReviewApplicationTask[]) => {
                this.tcTasks = response;
                this.rerender()

                this.SpinnerService.hide();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
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
        this.getAllDocs(String(this.actionRequest.id))
        if (task.hofId != null) {
            this.getSelectedUser(task.hofId)
        }

        // @ts-ignore
        container.appendChild(button);
        button.click();
    }

    @ViewChild('closeViewModal') private closeModal: ElementRef;

    public hideModel() {
        this.closeModal?.nativeElement.click();
    }

    @ViewChild('editForm') private mytemplateForm: NgForm;

    public clearForm() {
        this.mytemplateForm?.resetForm();
    }

    showToasterError(title: string, message: string) {
        this.notifyService.showError(message, title)

    }



    viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
        this.SpinnerService.show();
        this.membershipToTcService.viewDEditedApplicationPDF(pdfId, "ApplicantCV").subscribe(
            (dataPdf: any) => {
                this.SpinnerService.hide();
                this.blob = new Blob([dataPdf], {type: applicationType});

                // tslint:disable-next-line:prefer-const
                let downloadURL = window.URL.createObjectURL(this.blob);
                window.open(downloadURL, fileName);
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


        });

    }

    ngOnDestroy(): void {
        // Do not forget to unsubscribe the event
        this.dtTrigger.unsubscribe();


    }

    public getAllDocs(applicationId: string): void {
        this.membershipToTcService.getUserCV(applicationId, "UPLOADS", "ApplicantCV").subscribe(
            (response: Document[]) => {
                this.docs = response;
                this.rerender()


            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    private getSelectedUser(userId) {
        this.route.fragment.subscribe(params => {
            this.masterService.loadUserDetails(userId).subscribe(
                (data: UserRegister) => {
                    this.userDetails = data;
                }
            );

        });
    }


}
