import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {SchemeMembership, UsersEntity} from "../../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SchemeMembershipService} from "../../../../core/store/data/std/scheme-membership.service";
import {UserEntityService} from "../../../../core/store";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";
import Swal from "sweetalert2";
import {formatDate} from "@angular/common";

@Component({
    selector: 'app-scheme-membership-sic',
    templateUrl: './scheme-membership-sic.component.html',
    styleUrls: ['./scheme-membership-sic.component.css']
})
export class SchemeMembershipSicComponent implements OnInit {
    dtOptions: DataTables.Settings = {};
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();
    public tcSecs !: UsersEntity[];
    dateFormat = "yyyy-MM-dd";
    language = "en";
    p = 1;
    p2 = 1;
    public tcTasks: SchemeMembership[] = [];
    public actionRequest: SchemeMembership | undefined;
    stdHOFReview: FormGroup;
    selectedNwi: string;
    public itemId: string = "";

    constructor(private schemeMembershipService: SchemeMembershipService,
                private service: UserEntityService,
                private standardDevelopmentService: StandardDevelopmentService,
                private SpinnerService: NgxSpinnerService,
                private notifyService: NotificationService,
                private formBuilder: FormBuilder,) {
    }

    ngOnInit(): void {
        this.getApplicationsForReview();
        this.stdHOFReview = this.formBuilder.group({
            varField1: ['', Validators.required],
            requestId: ['', Validators.required],

        });
    }

    public getApplicationsForReview(): void {
        this.schemeMembershipService.getSICTasks().subscribe(
            (response: SchemeMembership[]) => {
                console.log(response);
                this.tcTasks = response;
                this.rerender()

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        )
    }

    public onOpenModal(task: SchemeMembership, mode: string): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        console.log(task.requestId)

        this.itemId = String(task.requestId);
        if (mode === 'edit') {
            this.stdHOFReview.reset();
            this.actionRequest = task;
            this.selectedNwi = String(this.actionRequest.requestId)
            this.stdHOFReview.controls.requestId.setValue(this.selectedNwi);

            button.setAttribute('data-target', '#voteDecisionModal');
        }
        if (mode === 'webStore') {
            this.stdHOFReview.reset();
            this.actionRequest = task;
            this.selectedNwi = String(this.actionRequest.requestId)
            this.stdHOFReview.controls.requestId.setValue(this.selectedNwi);

            button.setAttribute('data-target', '#webStoreModal');
        }
        if (mode === 'webStoreAllDetails') {
            this.stdHOFReview.reset();
            this.actionRequest = task;
            this.selectedNwi = String(this.actionRequest.requestId)
            this.stdHOFReview.controls.requestId.setValue(this.selectedNwi);

            button.setAttribute('data-target', '#webStoreAllDetailsModal');
        }
        container.appendChild(button);
        button.click();

    }


    @ViewChild('closeViewModal') private closeModal: ElementRef | undefined;

    public hideModel() {
        this.closeModal?.nativeElement.click();
    }

    rerender(): void {
        this.dtElements.forEach((dtElement: DataTableDirective) => {
            if (dtElement.dtInstance)
                dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
                    dtInstance.destroy();
                });
        });
        setTimeout(() => {
            this.dtTrigger1.next();


        });

    }

    ngOnDestroy(): void {
        // Do not forget to unsubscribe the event
        this.dtTrigger1.unsubscribe();


    }

    public onReviewTask(): void {


        if (this.stdHOFReview.valid) {
            this.SpinnerService.show();

            this.schemeMembershipService.addToWebStore(this.stdHOFReview.value).subscribe(
                (response) => {
                    this.showToasterSuccess(response.httpStatus, `User Added To Webstore`);

                    this.SpinnerService.hide();

                    this.getApplicationsForReview();
                    this.stdHOFReview.reset();
                    this.hideModel()
                },
                (error: HttpErrorResponse) => {
                    alert(error.message);
                    this.SpinnerService.hide();

                }
            )
        } else {
            this.showToasterError("Error", `Please Fill In All The Fields.`);

        }
    }

    showToasterError(title: string, message: string) {
        this.notifyService.showError(message, title)

    }

    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)

    }

    public getTcSecs(): void {
        this.standardDevelopmentService.getTcSec().subscribe(
            (response: UsersEntity[]) => {
                this.tcSecs = response;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    generateInvoice(tcTask: SchemeMembership) {
        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: 'btn btn-success',
                cancelButton: 'btn btn-success'
            },
            buttonsStyling: false
        });

        swalWithBootstrapButtons.fire({
            title: 'Are you sure your want to generate an Invoice?',
            text: 'You won\'t be able to reverse this!',
            icon: 'success',
            showCancelButton: true,
            confirmButtonText: 'Yes!',
            cancelButtonText: 'No!',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                this.SpinnerService.show();
                this.schemeMembershipService.generateInvoice(tcTask).subscribe(
                    (response) => {
                        this.SpinnerService.hide();
                        swalWithBootstrapButtons.fire(
                            'Generated!',
                            'Invoice Generated!',
                            'success'
                        );
                        this.SpinnerService.hide();
                        this.showToasterSuccess(response.httpStatus, 'Invoice Generated');
                        this.getApplicationsForReview();
                    },
                );
            }
        });


    }

    confirmPayment(tcTask: SchemeMembership) {

        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: 'btn btn-success',
                cancelButton: 'btn btn-success'
            },
            buttonsStyling: false
        });

        swalWithBootstrapButtons.fire({
            title: 'Are you sure your want to confirm receipt of Payment?',
            text: 'You won\'t be able to reverse this!',
            icon: 'success',
            showCancelButton: true,
            confirmButtonText: 'Yes!',
            cancelButtonText: 'No!',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                this.SpinnerService.show();
                this.schemeMembershipService.updatePayment(tcTask).subscribe(
                    (response) => {
                        this.SpinnerService.hide();
                        swalWithBootstrapButtons.fire(
                            'Confirmed!',
                            'Payment Received!',
                            'success'
                        );
                        this.SpinnerService.hide();
                        this.showToasterSuccess(response.httpStatus, 'Payment Received');
                        this.getApplicationsForReview();
                    },
                );
            }
        });


    }
    formatFormDate(date: Date) {
        return formatDate(date, this.dateFormat, this.language);
    }
}
