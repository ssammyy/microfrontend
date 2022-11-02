import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";
import {MatDialog} from "@angular/material/dialog";
import {ProductDetailsComponent} from "./product-details/product-details.component";
import {Router} from "@angular/router";
import {ConsignmentStatusComponent} from "../../../../core/shared/customs/consignment-status/consignment-status.component";
import {LocalDataSource} from "ng2-smart-table";

@Component({
    selector: 'app-importation-waiver',
    templateUrl: './importation-waiver.component.html',
    styleUrls: ['./importation-waiver.component.css']
})
export class ImportationWaiverComponent implements OnInit {
    public ApplicationDetailsForm: FormGroup;
    masterProductList = [];
    productList: LocalDataSource

    productSettings = {
        selectMode: 'single',  // single|multi
        hideHeader: false,
        hideSubHeader: false,
        actions: {
            columnTitle: 'Actions',
            add: false,
            edit: false,
            delete: false,
            custom: [
                //  { name: 'editRecord', title: '<i class="btn btn-sm btn-primary">View More</i>' },
                // {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">View More</i>'}
            ],
            position: 'right' // left|right
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true
        },
        noDataMessage: 'No Product Added',
        columns: {
            hsCode: {
                title: 'HS Code',
                type: 'string',
                filter: false
            },
            productDescription: {
                title: 'Product Description',
                type: 'string',
                filter: false
            },
            countryOfOrigin: {
                title: 'Country of Origin',
                type: 'string'
            },
            productUnit: {
                title: 'units',
                type: 'string'
            },
            quantity: {
                title: 'Quantity',
                type: 'string'
            },
            totalAmount: {
                title: 'Total Amount',
                type: 'custom',
                renderComponent: ConsignmentStatusComponent
            }
        },
        pager: {
            display: true,
            perPage: 20
        }
    };
    public finishedFillingFirstForm: boolean = false;
    public firstFormButtonClicked: boolean = false;
    private selectedFiles: File[] = []
    private loading = false
    public applicantFormErrors = {
        contactPersonPhone: '',
        contactPersonEmail: '',
        contactPersonName: '',
        applicantName: '',
        phoneNumber: '',
        postalAddress: '',
        email: '',
        kraPin: ''
    };

    public secondFormData = {
        category: '',
        justification: '',
        productDescription: '',
        documentation: [],
        masterList: []
    }


    categoryList = [
        {value: 'cat-0', viewValue: 'Category 1'},
        {value: 'cat-1', viewValue: 'Category 2'},
        {value: 'cat-2', viewValue: 'Category 3'}
    ];

    descriptionList = [
        {value: 'desc-0', viewValue: 'Description 1'},
        {value: 'desc-1', viewValue: 'Description 2'},
        {value: 'desc-2', viewValue: 'Description 3'}
    ];

    masterDescriptionList = [
        {value: 'desc-0', viewValue: 'Master Description 1'},
        {value: 'desc-1', viewValue: 'Master Description 2'},
        {value: 'desc-2', viewValue: 'Master Description 3'}
    ];

    masterCountryList = [
        {value: 'ken-0', viewValue: 'Kenya'},
        {value: 'ug-1', viewValue: 'Uganda'},
        {value: 'tz-2', viewValue: 'Tanzania'}
    ];

    masterCurrencyList = [
        {value: 'ken-0', viewValue: 'KES'},
        {value: 'ug-1', viewValue: 'UGA'},
        {value: 'tz-2', viewValue: 'Tz'}
    ]

    errors: any

    constructor(
        public formBuilder: FormBuilder,
        private pvoc: PVOCService,
        private dialog: MatDialog,
        private router: Router
    ) {
    }

    ngOnInit() {
        this.masterProductList = []
        this.productList = new LocalDataSource(this.masterProductList);
        this.createForm();
        this.loadDefaults()
    }

    createForm() {
        // Step 2:
        this.ApplicationDetailsForm = this.formBuilder.group({
            contactPersonName: new FormControl(null, Validators.required),
            contactPersonPhone: new FormControl(null, Validators.required),
            contactPersonEmail: new FormControl(null, [Validators.required, Validators.email]),
            category: new FormControl(null, Validators.required),
            justification: new FormControl('', Validators.required)
        })
    }

    importFile($event) {
        if ($event.target.files.length == 0) {
            console.log("No file selected!");
            return
        }
        let file: File = $event.target.files[0];
        // console.log("file selected!",$event.target.files,  file.name);
        this.secondFormData.documentation.push({
            name: file.name,
            file
        });
        this.selectedFiles.push(file)
        // console.log("file selected!", this.secondFormData.documentation);

    }

    submitDataFunction() {
        let data = this.ApplicationDetailsForm.value;
        data["products"] = this.masterProductList
        console.log(data)
        this.loading = true;
        this.pvoc.applyForImportWaiver(data, this.selectedFiles)
            .subscribe(
                res => {
                    if (res.responseCode === '00') {
                        this.pvoc.showSuccess(res.message, () => this.goBackHome())
                    } else {
                        this.pvoc.showError(res.message, null)
                        this.errors = res.errors
                        this.loading = false;
                    }
                },
                error => {
                    console.log(error)
                    this.loading = false;
                }
            )
    }

    goBackHome() {
        this.router.navigate(["/company/applications"])
            .then(() => {

            })
    }

    //Submit master list
    addMasterListDataToMasterList() {

    }

    addNewMasterItem() {
        let ref = this.dialog.open(ProductDetailsComponent, {
            data: {
                descriptions: this.masterDescriptionList,
                currencies: this.masterCurrencyList,
                countries: this.masterCountryList,
            }
        });
        // Get product data
        ref.afterClosed()
            .subscribe(
                res => {
                    console.log(res)
                    if (res) {
                        this.productList.append(res).then(() => console.log("DDD"))
                        //this.masterProductList.push(res)
                    }
                }
            )
    }

    deleteFile(i) {
        this.secondFormData.documentation.splice(i, 1);
    }

    // return list of error messages
    public validationMessages() {
        const messages = {
            required: 'This field is required',
            email: 'This email address is invalid',
            invalid_characters: (matches: any[]) => {

                let matchedCharacters = matches;

                matchedCharacters = matchedCharacters.reduce((characterString, character, index) => {
                    let string = characterString;
                    string += character;

                    if (matchedCharacters.length !== index + 1) {
                        string += ', ';
                    }

                    return string;
                }, '');

                return `These characters are not allowed: ${matchedCharacters}`;
            },
        };

        return messages;
    }

    private loadDefaults() {
        //Load categories
        this.pvoc.getWaiverCategories().subscribe(
            res => {
                if (res.responseCode == "00") {
                    this.categoryList = res.data;
                } else {
                    console.log(res.message)
                    this.categoryList = []
                }
            }
        )
    }
}
