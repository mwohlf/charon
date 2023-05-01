// properties of a page component
// required roles, title, icon...
export type PageConfig = {
  route: string;
  icon: string;
  title: string;
  // non-null / empty array means any authenticated user
  // null doesn't require an authenticated user to show this in the main menu
  requiredRoles: string[] | null;
  component: object;
}
