package com.arman.kotboy.core.debug.gui

import java.awt.Container
import java.awt.Dimension
import java.awt.GridLayout


class FlowGridLayout(rows: Int, cols: Int, hgap: Int = 0, vgap: Int = 0) : GridLayout(rows, cols, hgap, vgap) {

    override fun preferredLayoutSize(parent: Container): Dimension {
        synchronized(parent.treeLock) {
            val insets = parent.insets
            val ncomponents = parent.componentCount
            var nrows = rows
            var ncols = columns
            if (nrows > 0) {
                ncols = (ncomponents + nrows - 1) / nrows
            } else {
                nrows = (ncomponents + ncols - 1) / ncols
            }
            val w = IntArray(ncols)
            val h = IntArray(nrows)
            for (i in 0 until ncomponents) {
                val r = i / ncols
                val c = i % ncols
                val comp = parent.getComponent(i)
                val d = comp.preferredSize
                if (w[c] < d.width) {
                    w[c] = d.width
                }
                if (h[r] < d.height) {
                    h[r] = d.height
                }
            }
            var nw = 0
            for (j in 0 until ncols) {
                nw += w[j]
            }
            var nh = 0
            for (i in 0 until nrows) {
                nh += h[i]
            }
            return Dimension(
                insets.left + insets.right + nw + (ncols - 1) * hgap,
                insets.top + insets.bottom + nh + (nrows - 1) * vgap
            )
        }
    }

    override fun minimumLayoutSize(parent: Container): Dimension {
        synchronized(parent.treeLock) {
            val insets = parent.insets
            val ncomponents = parent.componentCount
            var nrows = rows
            var ncols = columns
            if (nrows > 0) {
                ncols = (ncomponents + nrows - 1) / nrows
            } else {
                nrows = (ncomponents + ncols - 1) / ncols
            }
            val w = IntArray(ncols)
            val h = IntArray(nrows)
            for (i in 0 until ncomponents) {
                val r = i / ncols
                val c = i % ncols
                val comp = parent.getComponent(i)
                val d = comp.minimumSize
                if (w[c] < d.width) {
                    w[c] = d.width
                }
                if (h[r] < d.height) {
                    h[r] = d.height
                }
            }
            var nw = 0
            for (j in 0 until ncols) {
                nw += w[j]
            }
            var nh = 0
            for (i in 0 until nrows) {
                nh += h[i]
            }
            return Dimension(
                insets.left + insets.right + nw + (ncols - 1) * hgap,
                insets.top + insets.bottom + nh + (nrows - 1) * vgap
            )
        }
    }

    override fun layoutContainer(parent: Container) {
        synchronized(parent.treeLock) {
            val insets = parent.insets
            val ncomponents = parent.componentCount
            var nrows = rows
            var ncols = columns
            if (ncomponents == 0) {
                return
            }
            if (nrows > 0) {
                ncols = (ncomponents + nrows - 1) / nrows
            } else {
                nrows = (ncomponents + ncols - 1) / ncols
            }
            val hgap = hgap
            val vgap = vgap
            // scaling factors
            val pd = preferredLayoutSize(parent)
            val sw = 1.0 * parent.width / pd.width
            val sh = 1.0 * parent.height / pd.height
            // scale
            val w = IntArray(ncols)
            val h = IntArray(nrows)
            for (i in 0 until ncomponents) {
                val r = i / ncols
                val c = i % ncols
                val comp = parent.getComponent(i)
                val d = comp.preferredSize
                d.width = (sw * d.width).toInt()
                d.height = (sh * d.height).toInt()
                if (w[c] < d.width) {
                    w[c] = d.width
                }
                if (h[r] < d.height) {
                    h[r] = d.height
                }
            }
            var c = 0
            var x = insets.left
            while (c < ncols) {
                var r = 0
                var y = insets.top
                while (r < nrows) {
                    val i = r * ncols + c
                    if (i < ncomponents) {
                        parent.getComponent(i).setBounds(x, y, w[c], h[r])
                    }
                    y += h[r] + vgap
                    r++
                }
                x += w[c] + hgap
                c++
            }
        }
    }

}