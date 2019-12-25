package org.eclipsercp.demo3d;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISizeProvider;
import org.eclipse.ui.part.ViewPart;
import ribbon.*;

public class RibbonView extends ViewPart {
	public static final String ID ="org.eclipsercp.demo3d.ribbonview";
	private RTabFolder rTabFolder;
	private RTabItem mainPageItem;
	private RTabItem layoutItem;
	private RTabItem visibleItem;
	private RMenu rMenu;
	private RMainButton fileButton;

	public RibbonView() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
		rTabFolder = new RTabFolder(parent);
		mainPageItem =new RTabItem(rTabFolder,"主页");
		layoutItem = new RTabItem(rTabFolder, "布局");
		visibleItem = new RTabItem(rTabFolder, "可视化");
		fileButton = new RMainButton(rTabFolder, "File",parent);
		System.out.println(parent.getDisplay().getShells()[0].getLocation());
		rMenu = new RMenu(fileButton);
		RMenuItem new_ = new RMenuItem(rMenu, SWT.PUSH);
		new_.setText("&New");
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public Object getAdapter(Class adapter) {
		if (ISizeProvider.class == adapter) {
			return new ISizeProvider() {
				public int getSizeFlags(boolean width) {
					return SWT.MIN | SWT.MAX | SWT.FILL;
				}

				public int computePreferredSize(boolean height, int availableParallel,
												int availablePerpendicular, int preferredResult) {
					return height ? 1000 : preferredResult;
				}
			};
		}
		return super.getAdapter(adapter);
	}
}
