import {Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import * as CryptoJS from 'crypto-js';
import {DataHolder} from "../../../core/store/data/std/request_std.model";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {FormBuilder, FormGroup} from "@angular/forms";
import {
    Department,
    TechnicalCommittee,
    TechnicalCommitteeBc,
    UsersEntity
} from "../../../core/store/data/std/std.model";
import {IDropdownSettings} from "ng-multiselect-dropdown";
import {StandardDevelopmentService} from "../../../core/store/data/std/standard-development.service";
import {Store} from "@ngrx/store";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import Swal from "sweetalert2";
import swal from "sweetalert2";

@Component({
    selector: 'app-manage-tc-members',
    templateUrl: './manage-tc-members.component.html',
    styleUrls: ['./manage-tc-members.component.css']
})
export class ManageTcMembersComponent implements OnInit {
    tcId: any;
    fullname = '';
    p = 1;
    p2 = 1;
    title = 'toaster-not';
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    @ViewChild(DataTableDirective, {static: false})
    dtElement: DataTableDirective;
    isDtInitialized: boolean = false
    public itemId: number;

    filteredTcSec: number;

    public createTCFormGroup!: FormGroup;

    public tcs !: TechnicalCommitteeBc[];
    public departments !: Department[];
    public technicalCommittees !: TechnicalCommittee[];
    public tscsecRequest !: DataHolder | undefined;
    loading = false;
    loadingText: string;
    public tcSecs !: UsersEntity[];

    public dropdownSettings: IDropdownSettings = {};
    dropdownList: any[] = [];
    selectedItems: UsersEntity[] = [];


    constructor(
        private formBuilder: FormBuilder,
        private standardDevelopmentService: StandardDevelopmentService,
        private store$: Store<any>,
        private router: Router,
        private notifyService: NotificationService,
        private SpinnerService: NgxSpinnerService,
        private route: ActivatedRoute,
    ) {
    }


    ngOnInit(): void {
        this.route.paramMap.subscribe(paramMap => {
            let key = '11A1764225B11AA1';
            const encrypted = paramMap.get('id');
            key = CryptoJS.enc.Utf8.parse(key);
            const decrypted = CryptoJS.AES.decrypt({ciphertext: CryptoJS.enc.Hex.parse(encrypted)}, key, {
                mode: CryptoJS.mode.ECB,
                padding: CryptoJS.pad.ZeroPadding,
            });
            this.tcId = decrypted.toString(CryptoJS.enc.Utf8);

        });
        this.getTechnicalCommittee(this.tcId);

    }

    public getTechnicalCommittee(tcId: any): void {
        const technicalCommittee: TechnicalCommitteeBc = {
            id: tcId,
            technical_committee_no: '',
            title: '',
            first_NAME: '',
            last_NAME: '',
            tc_TITLE: '',
            last_MODIFIED_ON: '',
            tc_ID: tcId,
            user_ID: tcId,
            organisation: '',
            principal:''

        };

        this.loadingText = "Retrieving Please Wait ...."
        this.loading = true;
        this.SpinnerService.show();

        this.standardDevelopmentService.getSpecificAllTcsWithMembers(technicalCommittee).subscribe(
            (response: TechnicalCommitteeBc[]) => {
                this.tcs = response;
                this.loading = false;
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
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.loading = false;
                this.SpinnerService.hide();
            }
        )
    }


    removeMember(tc: number) {
        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: 'btn btn-success',
                cancelButton: 'btn btn-success'
            },
            buttonsStyling: false
        });

        swalWithBootstrapButtons.fire({
            title: 'Are you sure your want to Remove This Member?',
            text: 'You won\'t be able to reverse this!',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Yes!',
            cancelButtonText: 'No!',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                this.SpinnerService.show();
                this.standardDevelopmentService.deleteTcMember(tc).subscribe(
                    (response) => {
                        this.SpinnerService.hide();
                        swalWithBootstrapButtons.fire(
                            'Success!',
                            'Successfully Deleted!',
                            'success'
                        );
                        this.SpinnerService.hide();
                        this.getTechnicalCommittee(this.tcId);
                    },
                );
            } else if (
                /* Read more about handling dismissals below */
                result.dismiss === swal.DismissReason.cancel
            ) {
                swalWithBootstrapButtons.fire(
                    'Cancelled',
                    'You have cancelled this operation',
                    'error'
                );
            }
        });

    }

    setAsPrincipal(tc: number) {
        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: 'btn btn-success',
                cancelButton: 'btn btn-success'
            },
            buttonsStyling: false
        });

        swalWithBootstrapButtons.fire({
            title: 'Are you sure your want to Set This Member As Principal?',
            icon: 'success',
            showCancelButton: true,
            confirmButtonText: 'Yes!',
            cancelButtonText: 'No!',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                this.SpinnerService.show();
                this.standardDevelopmentService.setAsPrincipal(tc).subscribe(
                    (response) => {
                        this.SpinnerService.hide();
                        swalWithBootstrapButtons.fire(
                            'Success!',
                            'Successfully Set As Principal!',
                            'success'
                        );
                        this.SpinnerService.hide();
                        this.getTechnicalCommittee(this.tcId);
                    },
                );
            } else if (
                /* Read more about handling dismissals below */
                result.dismiss === swal.DismissReason.cancel
            ) {
                swalWithBootstrapButtons.fire(
                    'Cancelled',
                    'You have cancelled this operation',
                    'error'
                );
            }
        });

    }
}
