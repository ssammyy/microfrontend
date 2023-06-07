export class SideBarMainMenus {
    path: string;
    title: string;
    type: string;
    icontype: string;
    collapse: string;
    children: SideBarChildMenus[];
}

export class SideBarChildMenus {
    path: string;
    title: string;
    ab: string;
}
